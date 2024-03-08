#!/bin/bash

# 8080 포트와 5173 포트의 프로세스 확인
echo "현재 8080 포트를 사용하는 프로세스:"
lsof -i :8080

echo "현재 5173 포트를 사용하는 프로세스:"
lsof -i :5173

# 사용자로부터 확인 메시지 받기
read -p "프로세스를 종료하시겠습니까? (y/n): " choice

# 사용자가 y를 입력한 경우에만 프로세스 종료
if [ "$choice" == "y" ]; then
    echo "프로세스를 종료합니다..."
    # 8080 포트 프로세스 종료
    sudo lsof -t -i :8080 | xargs sudo kill -9
    # 5173 포트 프로세스 종료
    sudo lsof -t -i :5173 | xargs sudo kill -9
    echo "프로세스가 종료되었습니다."
else
    echo "프로세스 종료를 취소했습니다."
fi
