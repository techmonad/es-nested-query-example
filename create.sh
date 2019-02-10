curl -XPUT "http://localhost:9200/testing" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "_doc": {
      "properties": {
        "title": {
          "type": "text"
        },
        "languages": {
          "type": "nested",
          "properties": {
            "id": {
              "type": "integer"
            },
            "name": {
              "type": "text"
            },
            "level": {
              "type": "integer"
            }
          }
        }
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/testing/_doc/1" -H 'Content-Type: application/json' -d'
{
  "title": "my first document",
  "languages": [

    {
      "id": 1,
      "name": "English",
      "level": 2
    },

    {
      "id": 2,
      "name": "German",
      "level": 1
    }
  ]
}'


curl -XPUT "http://localhost:9200/testing/_doc/2" -H 'Content-Type: application/json' -d'
{
  "title": "my second document",
  "languages": [

    {
      "id": 1,
      "name": "English",
      "level": 2
    },

    {
      "id": 3,
      "name": "Spanish",
      "level": 2
    }
  ]
}'


curl -XPUT "http://localhost:9200/testing/_doc/2" -H 'Content-Type: application/json' -d'
{
  "title": "my second document",
  "languages": [

    {
      "id": 1,
      "name": "English",
      "level": 2
    },

    {
      "id": 4,
      "name": "Hindi",
      "level": 2
    }
  ]
}'

## search query convert into java query DSL EsQueryApp.scala
curl -XGET "http://localhost:9200/testing/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "filter": [
        {
          "bool": {
            "must": [
              {
                "nested": {
                  "path": "languages",
                  "query": {
                    "bool": {
                      "must": [
                        {
                          "match": {
                            "languages.id": 1
                          }
                        },
                        {
                          "range": {
                            "languages.level": {
                              "gte": 0
                            }
                          }
                        }
                      ]
                    }
                  }
                }
              },
              {
                "nested": {
                  "path": "languages",
                  "query": {
                    "bool": {
                      "must": [
                        {
                          "match": {
                            "languages.id": 2
                          }
                        },
                        {
                          "range": {
                            "languages.level": {
                              "gte": 0
                            }
                          }
                        }
                      ]
                    }
                  }
                }
              }
            ]
          }
        }
      ]
    }
  }
}'


