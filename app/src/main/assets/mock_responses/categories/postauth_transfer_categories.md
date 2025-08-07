## API: Post Auth - List of Transfer Categories

**Host:** `www.finsible.app`
**Path:** `/categories?type=transfer`
**Method:** `GET`
**Status:** `200 OK`

```json
{
  "success": true,
  "message": "Transfer categories",
  "data": {
    "type": "TRANSFER",
    "categories": [
      {
        "id": 301001,
        "name": "Account Transfer",
        "color": "blue",
        "domain": "ACCOUNT_MANAGEMENT"
      },
      {
        "id": 301002,
        "name": "Cash Withdrawal",
        "color": "orange",
        "domain": "ACCOUNT_MANAGEMENT"
      },
      {
        "id": 301003,
        "name": "Cash Deposit",
        "color": "yellow",
        "domain": "ACCOUNT_MANAGEMENT"
      },
      {
        "id": 301004,
        "name": "Credit Card Payment",
        "color": "red",
        "domain": "ACCOUNT_MANAGEMENT"
      },
      {
        "id": 302001,
        "name": "Investment Purchase",
        "color": "green",
        "domain": "INVESTMENT"
      },
      {
        "id": 302002,
        "name": "Investment Sale",
        "color": "purple",
        "domain": "INVESTMENT"
      },
      {
        "id": 303001,
        "name": "Buy Asset",
        "color": "pink",
        "domain": "ASSET_ACQUISITION"
      },
      {
        "id": 303002,
        "name": "Sell Asset",
        "color": "gray",
        "domain": "ASSET_DISPOSAL"
      }
    ]
  }
}
```