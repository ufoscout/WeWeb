
# Run all tests
[group('test')]
test test_name="":
  cargo test {{test_name}}
  # cargo test {{test_name}} --all-features

# Cargo-leptos uses Playwright as the end-to-end test tool.  
# Tests are located in end2end/tests directory.
[group('test')]
end2end:
  cargo leptos end-to-end