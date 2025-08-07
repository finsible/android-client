## API: Post Auth - List of Accounts

**Host:** `www.finsible.app`
**Path:** `/accounts/all`
**Method:** `GET`
**Status:** `200 OK`

```json
{
  "success": true,
  "message": "Launch app: List of accounts",
  "data": {
    "accounts": [
      {
        "id": 901001,
        "name": "Cash",
        "balance": 0.00,
        "accountType": "CASH",
        "isCustom": false,
        "description": "Physical cash in wallet or at home"
      },
      {
        "id": 901002,
        "name": "Mutual Funds",
        "balance": 0.00,
        "accountType": "MUTUAL_FUNDS",
        "isCustom": false,
        "description": "SIP investments and lump sum mutual funds"
      },
      {
        "id": 901003,
        "name": "Stocks",
        "balance": 0.00,
        "accountType": "STOCKS",
        "isCustom": false,
        "description": "Individual stocks bought through demat account"
      },
      {
        "id": 901004,
        "name": "Fixed Deposits",
        "balance": 0.00,
        "accountType": "FD",
        "isCustom": false,
        "description": "Money locked in fixed deposits with banks"
      },
      {
        "id": 901005,
        "name": "Loans",
        "loanAmount": 0.00,
        "remainingAmount": 0.00,
        "accountType": "LOANS",
        "isCustom": false,
        "description": "Personal loans, home loans, or money borrowed"
      }
    ]
  },
  "cache": true,
  "cacheTtlMinutes": 1440
}
```