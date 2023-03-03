maven_jar(
    name = "org_lwjgl_lwjgl",
    artifact = "org.lwjgl:lwjgl:3.1.0",
)

maven_jar(
    name = "org_lwjgl_lwjgl_opengl",
    artifact = "org.lwjgl:lwjgl-opengl:3.1.0",
)

maven_jar(
    name = "org_lwjgl_lwjgl_glfw",
    artifact = "org.lwjgl:lwjgl-glfw:3.1.0",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

http_archive(
    name = "io_bazel_rules_go",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/rules_go/releases/download/v0.19.5/rules_go-v0.19.5.tar.gz",
        "https://github.com/bazelbuild/rules_go/releases/download/v0.19.5/rules_go-v0.19.5.tar.gz",
    ],
    sha256 = "513c12397db1bc9aa46dd62f02dd94b49a9b5d17444d49b5a04c5a89f3053c1c",
)

http_archive(
    name = "zlib",
    build_file = "@com_google_protobuf//:third_party/zlib.BUILD",
    sha256 = "c3e5e9fdd5004dcb542feda5ee4f0ff0744628baf8ed2dd5d66f8ca1197cb1a1",
    strip_prefix = "zlib-1.2.11",
    urls = ["https://zlib.net/fossils/zlib-1.2.11.tar.gz"],
)

load("@io_bazel_rules_go//go:deps.bzl", "go_download_sdk", "go_rules_dependencies", "go_register_toolchains")

go_rules_dependencies()
go_register_toolchains()

go_download_sdk(
    name = "go_sdk",
    sdks = {
        "windows_amd64": ("go1.13.15.windows-amd64.zip", "26c031d5dc2b39578943dbd34fe5c464ac4ed1c82f8de59f12999d3bf9f83ea1"),
        "linux_amd64":  ("go1.13.15.linux-amd64.tar.gz", "01cc3ddf6273900eba3e2bf311238828b7168b822bb57a9ccab4d7aa2acd6028"),
    },
)


http_archive(
    name = "bazel_gazelle",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/bazel-gazelle/releases/download/0.18.2/bazel-gazelle-0.18.2.tar.gz",
        "https://github.com/bazelbuild/bazel-gazelle/releases/download/0.18.2/bazel-gazelle-0.18.2.tar.gz",
    ],
    sha256 = "7fc87f4170011201b1690326e8c16c5d802836e3a0d617d8f75c3af2b23180c4",
)

load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies")

gazelle_dependencies()

load("//:repositories.bzl", "go_repositories")

go_repositories()

git_repository(
    name = "com_google_protobuf",
    commit = "09745575a923640154bcf307fba8aedff47f240a",
    remote = "https://github.com/protocolbuffers/protobuf",
    shallow_since = "1558721209 -0700",
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

load("@com_github_googleapis_googleapis//:repository_rules.bzl", "switched_rules_by_language")

switched_rules_by_language("com_google_googleapis_imports", go = True, grpc = True)
