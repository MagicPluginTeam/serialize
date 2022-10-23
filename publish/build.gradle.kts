plugins {
    java
    `java-library`
    `maven-publish`
}
lateinit var sourcesArtifact: PublishArtifact

dependencies {
    allprojects.forEach { if (it != project) api(it) }
}

tasks {
    artifacts {
        sourcesArtifact = archives(jar)
    }
}
publishing {
    val repo = System.getenv("GITHUB_REPOSITORY")
    if (repo === null) return@publishing
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
            credentials {

                username = System.getenv("SONATYPE_USERNAME") as? String
                password = System.getenv("SONATYPE_PASSWORD") as? String
            }
        }
    }
    publications {
        register<MavenPublication>(project.name) {
            val githubUserName = repo.substring(0, repo.indexOf("/"))
            groupId = "io.github.${githubUserName.toLowerCase()}"
            artifactId = rootProject.name.toLowerCase()
            version = System.getenv("GITHUB_BUILD_NUMBER")?: project.version.toString()
            artifact(sourcesArtifact)
        }
    }

}