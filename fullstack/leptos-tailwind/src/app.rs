use crate::{error_template::{AppError, ErrorTemplate}, server_fn::{get_cargo_toml, AddTodo}};
use leptos::*;
use leptos_meta::*;
use leptos_router::*;

#[component]
pub fn App() -> impl IntoView {
    // Provides context that manages stylesheets, titles, meta tags, etc.
    provide_meta_context();

    view! {


        // injects a stylesheet into the document <head>
        // id=leptos means cargo-leptos will hot-reload this stylesheet
        <Stylesheet id="leptos" href="/pkg/leptos-tailwind.css"/>

        // sets the document title
        <Title text="Welcome to Leptos"/>

        // content for this welcome page
        <Router fallback=|| {
            let mut outside_errors = Errors::default();
            outside_errors.insert_with_default_key(AppError::NotFound);
            view! {
                <ErrorTemplate outside_errors/>
            }
            .into_view()
        }>
            <main>
                <Routes>
                    <Route path="" view=HomePage/>
                    <Route path="cargo" view=CargoTomlPage/>
                    <Route path="todo" view=CargoTomlPage/>
                </Routes>
            </main>
        </Router>
    }
}

/// Renders the home page of your application.
#[component]
fn HomePage() -> impl IntoView {
    // Creates a reactive value to update the button
    let (count, set_count) = create_signal(0);
    let on_click = move |_| set_count.update(|count| *count += 1);

    view! {
        <h1>"Welcome to Leptos!"</h1>
        <button on:click=on_click>"Click Me: " {count}</button>
    }
}

#[component]
fn CargoTomlPage() -> impl IntoView {

    // Creates a reactive value to update the button
    let cargo_toml_resource = create_resource(|| (), |_| async move { get_cargo_toml().await });

    view! {
        <h1>"Cargo Toml file:"</h1>
        <Suspense
            // the fallback will show whenever a resource
            // read "under" the suspense is loading
            fallback=move || view! { <pre>"Loading..."</pre> }
        >
            // the children will be rendered once initially,
            // and then whenever any resources has been resolved
            <pre>
                {move || cargo_toml_resource.get()}
            </pre>
        </Suspense>
    }
}

#[component]
fn AddTodoPage() -> impl IntoView {

    // Creates a reactive value to update the button
    let post_action = create_server_action::<AddTodo>();

    view! {
        // <ul>
        //     {values.into_iter()
        //         .map(|n| view! { <li>{n}</li>})
        //         .collect::<Vec<_>>()}
        // </ul>
        <ActionForm action=post_action>
            <span>Add Todo:</span>
            <input type="text" name="title" placeholder="Enter a title"/>
            <button type="submit">Update</button>
        </ActionForm>
    }
}