#[cfg(feature = "ssr")]
#[tokio::main]
async fn main() {
    use axum::Router;
    use leptos::logging::log;
    use leptos::prelude::*;
    use leptos_axum::{generate_route_list, LeptosRoutes};
    use leptos_tailwind::app::*;
    use tower_sessions::{cookie::time::Duration, Expiry, MemoryStore, SessionManagerLayer};
    use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt, EnvFilter};

    // Initialize the logger
    {
        tracing_subscriber::registry()
            .with(EnvFilter::new(std::env::var("RUST_LOG").unwrap_or_else(
                |_| "info,leptos_tailwind=debug,tower_sessions=debug".into(),
            )))
            .with(tracing_subscriber::fmt::layer())
            .try_init()
            .expect("Failed to initialize logging");
    }

    // Initialize the session manager
    let session_layer = {
        // This uses `tower-sessions` to establish a layer that will provide the session
        // as a request extension.
        let session_store = MemoryStore::default();
        SessionManagerLayer::new(session_store)
            .with_secure(false)
            .with_expiry(Expiry::OnInactivity(Duration::days(1)))
    };

    // Conigure the Leptos App
    let conf = get_configuration(None).unwrap();
    let addr = conf.leptos_options.site_addr;
    let app = {
        let leptos_options = conf.leptos_options;
        // Generate the list of routes in your Leptos App
        let routes = generate_route_list(App);

        Router::new()
            .leptos_routes(&leptos_options, routes, {
                let leptos_options = leptos_options.clone();
                move || shell(leptos_options.clone())
            })
            .fallback(leptos_axum::file_and_error_handler(shell))
            .with_state(leptos_options)
    };

    let app = app
        .route("/counter", axum::routing::get(session::counter))
        .route("/clear_counter", axum::routing::get(session::clear_counter))
        .layer(session_layer);

    // run our app with hyper
    // `axum::Server` is a re-export of `hyper::Server`
    log!("listening on http://{}", &addr);
    let listener = tokio::net::TcpListener::bind(&addr).await.unwrap();
    axum::serve(listener, app.into_make_service())
        .await
        .unwrap();

    mod session {
        use axum::{
            extract::FromRequestParts, http::request::Parts, response::IntoResponse,
        };
        use serde::{Deserialize, Serialize};
        use tower_sessions::Session;

        const COUNTER_KEY: &str = "counter";

        #[derive(Default, Deserialize, Serialize)]
        pub struct Counter(usize);

        impl<S> FromRequestParts<S> for Counter
        where
            S: Send + Sync,
        {
            type Rejection = (axum::http::StatusCode, &'static str);

            async fn from_request_parts(
                req: &mut Parts,
                state: &S,
            ) -> Result<Self, Self::Rejection> {
                let session = Session::from_request_parts(req, state).await?;
                let counter: Counter = session.get(COUNTER_KEY).await.unwrap().unwrap_or_default();
                session.insert(COUNTER_KEY, counter.0 + 1).await.unwrap();
                Ok(counter)
            }
        }

        pub async fn clear_counter(session: Session) -> impl IntoResponse {
            session.delete().await.unwrap();
            "Counter cleared"
        }

        pub async fn counter(counter: Counter) -> impl IntoResponse {
            format!("Current count: {}", counter.0)
        }
    }
}

#[cfg(not(feature = "ssr"))]
pub fn main() {
    // no client-side main function
    // unless we want this to work with e.g., Trunk for pure client-side testing
    // see lib.rs for hydration function instead
}
