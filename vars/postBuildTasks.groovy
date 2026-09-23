def call(Map config = [:]) {
    def artifactPattern = config.artifactPattern

    if (artifactPattern) {
        stage('Archive artifacts') {
            archiveArtifacts artifacts: artifactPattern, fingerprint: true
        }
    }

    cleanWs()
}
