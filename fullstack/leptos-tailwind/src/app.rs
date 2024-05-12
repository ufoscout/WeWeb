use crate::{error_template::{AppError, ErrorTemplate}, server_fn::{add_todo, get_cargo_toml, AddTodo}};
use leptos::{html::title, *};
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
  
            <Title text="Leptos + Tailwindcss"/>
            <main>
                <Routes>
                    <Route path="" view=HomePage/>
                    <Route path="cargo" view=CargoTomlPage/>
                    <Route path="todo" view=AddTodoPage/>
                    <Route path="todo_type_safe" view=AddTodoTypeSafePage/>
                </Routes>
            </main>
        </Router>
    }
}

/// Renders the home page of your application.
#[component]
fn HomePage() -> impl IntoView {
    // Creates a reactive value to update the button
    let (value, set_value) = create_signal(0);

    view! {
        <h1>"Welcome to Leptos!"</h1>
        <div class="bg-gradient-to-tl from-blue-800 to-blue-500 text-white font-mono flex flex-col min-h-screen">
        <div class="flex flex-row-reverse flex-wrap m-auto">
            <button on:click=move |_| set_value.update(|value| *value += 1) class="rounded px-3 py-2 m-1 border-b-4 border-l-2 shadow-lg bg-blue-700 border-blue-800 text-white">
                "+"
            </button>
            <button class="rounded px-3 py-2 m-1 border-b-4 border-l-2 shadow-lg bg-blue-800 border-blue-900 text-white">
                {value}
            </button>
            <button on:click=move |_| set_value.update(|value| *value -= 1) class="rounded px-3 py-2 m-1 border-b-4 border-l-2 shadow-lg bg-blue-700 border-blue-800 text-white">
                "-"
            </button>
        </div>
    </div>
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

#[component]
fn AddTodoTypeSafePage() -> impl IntoView {
    
    let (title, set_title) = create_signal("".to_string());
    
    let on_click = move |_| {
        spawn_local(async move {
            let _result = add_todo(title()).await.unwrap();
        });
    };

    view! {
        <div>
            <span>Add Todo:</span>
            <input type="text"
                on:input=move |ev| {
                    set_title(event_target_value(&ev));
                }
                value=title
            />
            <button on:click=on_click>
                "Add Todo"
            </button>
        </div>
        
    }
}