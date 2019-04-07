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

git_repository(
    name = "org_pubref_rules_protobuf",
    remote "https://github.com/pubref/rules_protobuf.git",
    tag = "v0.6.0",
)

load("@org_pubref_rules_protobuf//go:rules.bzl", "go_proto_repositories")
go_proto_repositories()
