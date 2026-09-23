def call(Map config) {
    def projectKey = config.projectKey
    withSonarQubeEnv(config.sonarServer ?: 'SonarQube') {
        sh "mvn -B clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${projectKey}"
    }
}
