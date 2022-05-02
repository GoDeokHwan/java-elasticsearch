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

## Elastic 문법 
1. Boolean Query 문법
#### [ Elastic 문법 : SQL 문법 ]
- must : and
- should : or
- match : Like
- term : =
- terms : in
- range : 범위
  - gte : <=
  - lte : >=
  - gt : <
  - lt : >
  
ex.
```bigquery
GET boards/_search?
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "[컬럼]": "[조건값]"
          }
        }
        , {
            "range": {
                "[컬럼]": {
                    "gte": "2022-05-02"
                    , "lte": "2022-05-12"
                    , "format": "yyyy-mm-dd"
                }
            }
        }
      ]
    }
  }
}
```
2. aggs Query 문법
- date_histogram : 집계 범위 지정
- aggs : 그룹 


   ex.
```bigquery
GET boards/_search?
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "[컬럼]": "[조건값]"
          }
        }
      ]
    }
  },
  "aggs": {
    "[그룹명]": {
        "date_histogram": {
          "field": "createDate",
          "interval": "week"
        }
    },
    "aggs": {
        "[그룹명]": {
          "terms": {
            "field": "taskTypeIds",
            "size": 100, // 그룹에 종류 사이즈 
            "missing": "0"  // 없으면 표시하는 값 
          },
          "aggs": {
            "[그룹명]": {
              "terms": {
                "field": "incomingChannel",
                "size": 10
              }
            }
          }
        }
      }
  }
}
```