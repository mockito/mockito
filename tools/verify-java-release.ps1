param(
    [string]$Project = ":mockito-extensions:mockito-junit-jupiter",
    [int]$ExpectedMajor = 55,
    [string]$ClassRelativePath = ""
)

$ErrorActionPreference = "Stop"

Write-Host "Building project $Project (without tests) ..."
& .\gradlew "$Project:compileJava" -x test | Out-Null

# Map Gradle project path to directory path
$projectDir = ($Project.Trim(':') -replace ':','\\')
if (-not (Test-Path $projectDir)) {
    throw "Cannot resolve directory for project '$Project' → '$projectDir'"
}

$classesDir = Join-Path $projectDir "build\classes\java\main"
if (-not (Test-Path $classesDir)) {
    throw "Classes directory not found: $classesDir. Did compile succeed?"
}

if ([string]::IsNullOrWhiteSpace($ClassRelativePath)) {
    $cls = Get-ChildItem $classesDir -Recurse -Filter *.class | Select-Object -First 1
} else {
    $clsPath = Join-Path $classesDir $ClassRelativePath
    if (-not (Test-Path $clsPath)) { throw "Class not found: $clsPath" }
    $cls = Get-Item $clsPath
}

if (-not $cls) { throw "No .class file found under $classesDir" }

Write-Host "Inspecting bytecode: $($cls.FullName)"
$line = (javap -verbose $cls.FullName | Select-String 'major version').ToString()
if (-not $line) { throw "Failed to read major version via javap" }

if ($line -match 'major version:\s*(\d+)') {
    $major = [int]$Matches[1]
    Write-Host "Detected major version: $major"
    if ($major -ne $ExpectedMajor) {
        Write-Error "Expected major $ExpectedMajor but found $major"
        exit 1
    }
    Write-Host "Success: bytecode major version matches $ExpectedMajor"
    exit 0
} else {
    throw "Unable to parse major version from: $line"
}

