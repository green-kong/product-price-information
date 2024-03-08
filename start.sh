#!/bin/bash

# 함수 정의: 포트 사용 여부 확인
check_port() {
    lsof -i :$1 >/dev/null
    if [ $? -eq 0 ]; then
        echo "$1 포트가 현재 사용 중 입니다."
        exit 1
    fi
}

# 1. 포트 사용 여부 확인
check_port 5173
check_port 8080

# 2. 서버 실행
echo "서버를 실행합니다..."
cd musinsa-server || exit
./gradlew build
java -jar build/libs/musinsa-server-0.0.1-SNAPSHOT.jar &
sleep 5
if lsof -i :8080 >/dev/null; then
    echo "서버가 정상적으로 실행되었습니다."
else
    echo "서버가 실행되지 못했습니다."
    exit 1
fi

# 3. 프론트 실행
echo "프론트 서버를 실행합니다..."
cd ../musinsa-front || exit
npm install
npm run dev &
sleep 5
if ! curl -s http://localhost:5173 >/dev/null; then
    echo "프론트 서버를 실행하지 못했습니다."
    pkill -f "npm run dev"
    echo "서버를 종료합니다."
    pkill -f "java -jar build/libs/server.jar"
    exit 1
fi

# 4. 실행 문구 출력
echo "정상적으로 실행 되었습니다."
echo "프론트 접속 : http://localhost:5173"
echo "API 문서 : http://localhost:8080/api/docs"
