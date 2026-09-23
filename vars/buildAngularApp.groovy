def call(Map config = [:]) {
    def buildConfig = config.buildConfig ?: 'production'

    stage('Install dependencies') {
        sh 'npm ci'
    }

    stage('Lint') {
        sh 'npm run lint'
    }

    stage('Test') {
        sh "npm test -- --watch=false --browsers=ChromeHeadless"
    }

    stage('Build') {
        sh "npm run build -- --configuration ${buildConfig}"
    }
}
