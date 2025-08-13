# 复制教学视频到Android应用内部存储的PowerShell脚本
# 使用方法：在PowerShell中运行此脚本

# 设置源目录和目标目录
$sourceDir = "teaching video"
$targetDir = "app/src/main/assets/teaching_video"

# 检查源目录是否存在
if (-not (Test-Path $sourceDir)) {
    Write-Host "错误：源目录 '$sourceDir' 不存在！" -ForegroundColor Red
    exit 1
}

# 创建目标目录
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    Write-Host "创建目标目录: $targetDir" -ForegroundColor Green
}

# 复制所有视频文件
$videoFiles = Get-ChildItem -Path $sourceDir -Filter "*.mp4"
$copiedCount = 0

foreach ($file in $videoFiles) {
    $targetPath = Join-Path $targetDir $file.Name
    try {
        Copy-Item -Path $file.FullName -Destination $targetPath -Force
        Write-Host "复制: $($file.Name)" -ForegroundColor Green
        $copiedCount++
    } catch {
        Write-Host "复制失败: $($file.Name) - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n复制完成！共复制了 $copiedCount 个视频文件到 $targetDir" -ForegroundColor Green
Write-Host "现在您可以在Android应用中访问这些视频文件了。" -ForegroundColor Yellow
