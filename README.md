### Starting the application

```
sbt run
```

##### Application gets started at port 9000 by default

### Running Test cases

```
sbt test
```

### Support for following operations


#### 1) Query a country by name or code

##### 1.a) Query by name

```

GET     /country/name/:name 
```

#####** This end point also supports partial match. If country name is passes as "Andor" then the controller will return result for country Andorra**

###### Sample response 

```
{
    "country_name": "Andorra",
    "country_code": "AD",
    "airports": [
        {
            "airport_name": "Andorra la Vella Heliport",
            "airport_ident": "AD-ALV",
            "runways": []
        }
    ]
}
```
##### 1.b) Query by code

```

GET     /country/code/:code
```

###### Sample response 

```
{
    "country_name": "United Arab Emirates",
    "country_code": "AE",
    "airports": [
        {
            "airport_name": "Abu Dhabi Northeast Airport",
            "airport_ident": "OM11",
            "runways": [
                {
                    "id": 232775,
                    "airport reference": 2,
                    "airport identifier": "OM11",
                    "length in ft": 7080,
                    "surface": "ASP"
                }
            ]
        },
        {
            "airport_name": "Abu Dhabi International Airport",
            "airport_ident": "OMAA",
            "runways": [
                {
                    "id": 308419,
                    "airport reference": 5226,
                    "airport identifier": "OMAA",
                    "length in ft": 13452,
                    "surface": "ASP"
                },
                {
                    "id": 232764,
                    "airport reference": 5226,
                    "airport identifier": "OMAA",
                    "length in ft": 13452,
                    "surface": "ASP"
                }
            ]
        },
        ..
```




#### 2) Generate reports

##### 2.a) Top 10 countries with Most and Least airports

```

GET     /country/airports
```
###### Sample response 

```
[
    {
        "country_name": "Papua New Guinea",
        "country_code": "PG",
        "airports": 564
    },
    {
        "country_name": "Venezuela",
        "country_code": "VE",
        "airports": 589
    },
    {
        "country_name": "Colombia",
        "country_code": "CO",
        "airports": 644
    },
    {
        "country_name": "Germany",
        "country_code": "DE",
        "airports": 654
    },
    {
        "country_name": "Argentina",
        "country_code": "AR",
        "airports": 699
    },
    {
        "country_name": "France",
        "country_code": "FR",
        "airports": 782
    },
    {
        "country_name": "Australia",
        "country_code": "AU",
        "airports": 880
    },
    {
        "country_name": "Canada",
        "country_code": "CA",
        "airports": 1886
    },
    {
        "country_name": "Brazil",
        "country_code": "BR",
        "airports": 3822
    },
    {
        "country_name": "United States",
        "country_code": "US",
        "airports": 21306
    },
    {
        "country_name": "Montserrat",
        "country_code": "MS",
        "airports": 1
    },
    {
        "country_name": "Christmas Island",
        "country_code": "CX",
        "airports": 1
    },
    {
        "country_name": "Aruba",
        "country_code": "AW",
        "airports": 1
    },
    {
        "country_name": "Norfolk Island",
        "country_code": "NF",
        "airports": 1
    },
    {
        "country_name": "Sint Maarten",
        "country_code": "SX",
        "airports": 1
    },
    {
        "country_name": "Gambia",
        "country_code": "GM",
        "airports": 1
    },
    {
        "country_name": "Mayotte",
        "country_code": "YT",
        "airports": 1
    },
    {
        "country_name": "CuraÃ§ao",
        "country_code": "CW",
        "airports": 1
    },
    {
        "country_name": "Monaco",
        "country_code": "MC",
        "airports": 1
    },
    {
        "country_name": "Saint BarthÃ©lemy",
        "country_code": "BL",
        "airports": 1
    }
]
```

##### 2.b) Runways Surface per country

```

GET     /country/runways
```

###### Sample response 

```
[
    {
        "country_name": "Andorra",
        "country_code": "AD",
        "runways": []
    },
    {
        "country_name": "United Arab Emirates",
        "country_code": "AE",
        "runways": [
            "MAC",
            "Asphalt",
            "ASP"
        ]
    },
    {
        "country_name": "Afghanistan",
        "country_code": "AF",
        "runways": [
            "MAC",
            "GVL",
            "GRV",
            "Asphalt",
            "CON",
            "CONCRETE AND ASP",
            "ASP"
        ]
    },
    {
        "country_name": "Antigua and Barbuda",
        "country_code": "AG",
        "runways": [
            "MAC",
            "GVL",
            "GRV",
            "Asphalt",
            "CON",
            "CONCRETE AND ASP",
            "ASP"
        ]
    },
        ..
```

##### The miscroservice is deployed on localhost:9000 and uses play 2.6 as a web framework along with scala as programming language

##### TODO's

###### ~~1) Write test for Report Controller~~
###### 2) Report Controller should also list most frequently used runway identifiers