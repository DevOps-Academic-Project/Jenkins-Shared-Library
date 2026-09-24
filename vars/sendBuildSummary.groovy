def call(Map config = [:]) {
    def status = currentBuild.currentResult
    def statusColor = status == 'SUCCESS' ? '#2ecc71' : (status == 'UNSTABLE' ? '#f39c12' : '#e74c3c')
    def statusEmoji = status == 'SUCCESS' ? '✅' : (status == 'UNSTABLE' ? '⚠️' : '❌')
    def recipient = config.recipient ?: 'louayzorai24@gmail.com'

    def body = """
    <html>
    <body style="font-family: Arial, sans-serif;">
        <h2 style="color: ${statusColor};">${statusEmoji} Build ${status} — ${env.JOB_NAME} #${env.BUILD_NUMBER}</h2>
        <table style="border-collapse: collapse; width: 100%;">
            <tr><td style="padding: 8px; font-weight: bold;">Projet</td><td style="padding: 8px;">${env.JOB_NAME}</td></tr>
            <tr><td style="padding: 8px; font-weight: bold;">Build</td><td style="padding: 8px;">#${env.BUILD_NUMBER}</td></tr>
            <tr><td style="padding: 8px; font-weight: bold;">Statut</td><td style="padding: 8px; color: ${statusColor}; font-weight: bold;">${status}</td></tr>
            <tr><td style="padding: 8px; font-weight: bold;">Durée</td><td style="padding: 8px;">${currentBuild.durationString}</td></tr>
            <tr><td style="padding: 8px; font-weight: bold;">Déclenché par</td><td style="padding: 8px;">${currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Inconnu'}</td></tr>
        </table>
        <p><a href="${env.BUILD_URL}" style="color: #3498db;">Voir les logs complets du build</a></p>
        <p><a href="${env.BUILD_URL}console" style="color: #3498db;">Voir la console complète</a></p>
        <hr>
        <p style="color: #888; font-size: 12px;">Envoyé automatiquement par Jenkins — Projet DevOps ESPRIT</p>
    </body>
    </html>
    """

    emailext(
        subject: "${statusEmoji} [${env.JOB_NAME}] Build #${env.BUILD_NUMBER} — ${status}",
        body: body,
        mimeType: 'text/html',
        to: recipient
    )
}
