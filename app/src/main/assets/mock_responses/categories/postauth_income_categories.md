## API: Post Auth - List of Income Categories

**Host:** `www.finsible.app`
**Path:** `/categories?type=income`
**Method:** `GET`
**Status:** `200 OK`

```json
{
  "success": true,
  "message": "Income categories",
  "data": {
    "type": "INCOME",
    "categories": [
      {
        "id": 201001,
        "name": "Salary",
        "color": "green",
        "domain": "PRIMARY"
      },
      {
        "id": 201002,
        "name": "Business",
        "color": "blue",
        "domain": "PRIMARY"
      },
      {
        "id": 202001,
        "name": "Dividends",
        "color": "purple",
        "domain": "PASSIVE"
      },
      {
        "id": 202002,
        "name": "Interest",
        "color": "yellow",
        "domain": "PASSIVE"
      },
      {
        "id": 202003,
        "name": "Rental Income",
        "color": "orange",
        "domain": "PASSIVE"
      },
      {
        "id": 203001,
        "name": "Freelance",
        "color": "pink",
        "domain": "IRREGULAR"
      },
      {
        "id": 203002,
        "name": "Gifts Received",
        "color": "gray",
        "domain": "IRREGULAR"
      },
      {
        "id": 203003,
        "name": "Borrowed Funds",
        "color": "red",
        "domain": "IRREGULAR"
      }
    ]
  }
}
```