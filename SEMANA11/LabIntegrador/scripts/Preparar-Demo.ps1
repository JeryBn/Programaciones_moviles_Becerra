$ErrorActionPreference = 'Stop'
$projectId = 'demo-lab-becerra'
$authUrl = 'http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts'
$docsUrl = "http://127.0.0.1:8087/v1/projects/$projectId/databases/(default)/documents"

foreach ($rol in 'usuario', 'admin') {
    $email = "$rol@laboratorio.example"
    $body = @{ email = $email; password = 'CatalogoDemo!12'; returnSecureToken = $true } | ConvertTo-Json
    try {
        $account = Invoke-RestMethod "${authUrl}:signUp?key=demo" -Method Post -ContentType 'application/json' -Body $body
    } catch {
        if ($_.ErrorDetails.Message -notmatch 'EMAIL_EXISTS') { throw }
        $account = Invoke-RestMethod "${authUrl}:signInWithPassword?key=demo" -Method Post -ContentType 'application/json' -Body $body
    }
    $perfil = @{ fields = @{ rol = @{ stringValue = $rol }; nombre = @{ stringValue = $rol } } } | ConvertTo-Json -Depth 5
    Invoke-RestMethod "$docsUrl/usuarios/$($account.localId)" -Method Patch -ContentType 'application/json' -Headers @{ Authorization = 'Bearer owner' } -Body $perfil | Out-Null
    $visible = Invoke-RestMethod "$docsUrl/usuarios/$($account.localId)" -Headers @{ Authorization = "Bearer $($account.idToken)" }
    if ($visible.fields.rol.stringValue -ne $rol) { throw "Rol incorrecto: $rol" }
    Write-Output "Cuenta local verificada: $email"
}
