# What's this?

This small command-line tool can check the name of podcast episodes before they are uploaded
so they don't contain unusual types. This is especially useful when automatically uploading
sermons to the church's home page - with name-checker, the available categories are always observed.

# Build using Earthly

The CI build of this project uses [Earthly](https://docs.earthly.dev/), which in turn uses
container virtualization (e.g. Docker or Podman). You can also run the build locally (if you
have Earthly as well as an OCI compatible container engine installed) by executing
`earthly +build`. This will create a container with everything needed for the build,
create the package inside it and then copy the results to the directory `target` for you.
