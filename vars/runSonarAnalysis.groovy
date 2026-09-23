def call(Map config) {
    def projectKey = config.projectKey
    withSonarQubeEnv(config.sonarServer ?: 'SonarQube') {
        sh "mvn -B clean verify sonar:sonar -Dsonar.projectKey=${projectKey}"
    }
}
