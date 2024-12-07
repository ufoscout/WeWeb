
# Cleans the build artifacts
[group('build')]
[confirm("Are you sure you want to clean the build artifacts?")]
clean:
  cargo clean


# Builds all artifacts.
# Generates your server binary in target/server/release and your site package in target/site.
# Copy these files to your remote server. The directory structure should be:
# ```text
# lightspeed
# site/
# ```
[group('build')]
build:
  cargo leptos build --release


# Watches for changes and rebuilds. This includes the tailwind CSS and the server binary.
# Once your app has compiled you can open up your browser to http://localhost:3000 to see it.
[group('build')]
watch:
  cargo leptos watch
