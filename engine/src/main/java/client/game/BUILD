java_library(
    name = "game",
    srcs = glob(["*.java"]),
    deps = [
        "//engine/src/main/java/client/graphics",
        "//engine/src/main/java/common/game",
        "@org_lwjgl_lwjgl//jar",
        "@org_lwjgl_lwjgl_opengl//jar",
        "@org_lwjgl_lwjgl_glfw//jar",
    ],
    resources = [
        "//engine/src/main/resources:resources",
    ],
    visibility = ["//visibility:public"],
)

java_binary(
    name = "client",
    srcs = ["Main.java"],
    main_class = "client.game.Main",
    deps = [
        ":game",
        "//engine/src/main/java/client/graphics",
    ],
    visibility = ["//visibility:public"],
)
