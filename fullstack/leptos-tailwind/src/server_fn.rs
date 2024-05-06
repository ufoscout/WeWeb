use leptos::*;

// This runs on the server
#[server]
pub async fn add_todo(title: String) -> Result<Vec<String>, ServerFnError> {
    println!("Adding todo: {}", title);
    Ok(vec![title])
}

#[server]
pub async fn get_cargo_toml() -> Result<String, ServerFnError> {
    println!("Reading Cargo.toml");
    let cargo_toml = tokio::fs::read_to_string("./Cargo.toml").await?;
    println!("Read Cargo.toml: {cargo_toml}");
    Ok(cargo_toml)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[tokio::test]
    async fn test_read_from_fs() {
        let cargo_toml = get_cargo_toml().await.unwrap();
        assert!(cargo_toml.contains(r#"name = "leptos-tailwind""#));
    }
}