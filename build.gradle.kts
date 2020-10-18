plugins {
    java
    id("com.github.johnrengelman.shadow").version("6.0.0")
}

group = "me.moros"
version = "1.0.0"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    compileOnly("me.moros", "Atlas", "1.0.0")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.3-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(rootProject.name)
        dependencies {
            relocate("me.moros.example", "me.moros.ares.example")
        }
    }
    build {
        dependsOn(shadowJar)
    }
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    named<Copy>("processResources") {
        filesMatching("plugin.yml") {
            expand("pluginVersion" to project.version)
        }
    }
}