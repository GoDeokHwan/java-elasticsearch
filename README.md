# java-elasticsearch
java ElasticSearch 테스트 개발 develop

# API 모듈
## 구성
- config : 전체 설정
- controller : Controller
- dto : DTO 모음
- entity : Table Entity
- event : ApplicationEvent 설정
- repository : Entity Repository
- service : Service 모음

## Mysql 설정
resources 밑에 mysql 폴더에 docker설정과 테이블 스키마 정보들이 있습니다.

# elastic 모듈

## 구성
- client
    - indexer : Elastic Client로 실제 연결하는 정보 
    - worker : Elastic Worker로 Elastic을 연결하는 Client정보
- config : elastic 기본 설정
- entity : elastic Index Entity 설정
- service : 외부에서 호출하는 service

## Elastic 설정
resources 밑에 docker 폴더 쪽에는 PC에 설치하는 Docker 설정과 Index DDL 정보가 들어 있습니다.
