@echo off
echo 正在复制教学视频文件...

REM 创建目标目录
if not exist "app\src\main\assets\teaching_video" mkdir "app\src\main\assets\teaching_video"

REM 复制所有MP4文件
xcopy "teaching video\*.mp4" "app\src\main\assets\teaching_video\" /Y

echo 复制完成！
echo 视频文件已复制到 app\src\main\assets\teaching_video\ 目录
pause
