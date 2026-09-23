def call(Map config = [:]) {
    def skipTests = config.skipTests ?: false

    stage('Build') {
        sh 'mvn -B clean compile'
    }

    stage('Test') {
        sh 'mvn -B test'
        junit '**/target/surefire-reports/*.xml'
    }

    stage('Package') {
        def skipFlag = skipTests ? '-DskipTests' : ''
        sh "mvn -B package ${skipFlag}"
    }
}
