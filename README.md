# Google Spreadsheets Java input plugin for Embulk

Embulk input plugin to load records from Google Spreadsheets.

## Overview

* **Plugin type**: input

## Configuration

| name                     | type    | requirement | default                                 | description |
|:-------------------------|:--------|:------------|:----------------------------------------|:------------|
| auth_method              | string  | optional    | `authorized_user`                       | `service_account`, `authorized_user`, `compute_engine`, or `application_default` |
| json_keyfile             | string  | optional    |                                         | keyfile path or `content` |
| spreadsheets_url         | string  | required    |                                         | |
| worksheet_title          | string  | required    |                                         | worksheet title |
| start_column             | integer | optional    | `1`                                     | |
| start_row                | integer | optional    | `1`                                     | |
| end_row                  | integer | optional    | `-1`                                    | `-1` means loading records until an empty record appears. |
| max_fetch_rows           | integer | optional    | `10000`                                 | Load data from a worksheet for each numerical value specified by this option. |
| null_string              | string  | optional    | `''`                                    | Replace this value to `NULL` |
| boolean_true_values      | array   | optional    | `['true', 't', 'yes', 'y', 'on', '1']`  | List of strings to treat as boolean `true`. These are case-insensitive. |
| boolean_false_values     | array   | optional    | `['false', 'f', 'no', 'n', 'off', '0']` | List of strings to treat as boolean `false`. These are case-insensitive. |
| value_render_option      | string  | optional    | `'FORMATTED_VALUE'`                     | `'FORMATTED_VALUE'`, `'UNFORMATTED_VALUE'`, `'FORMULA'` are available. See [the `value_render_option` document](https://developers.google.com/sheets/api/reference/rest/v4/ValueRenderOption). |
| date_time_render_option  | string  | optional    | `'SERIAL_NUMBER'`                       | `'SERIAL_NUMBER'`, `'FORMATTED_STRING'` are available. See [the `date_time_render_option` document](https://developers.google.com/sheets/api/reference/rest/v4/DateTimeRenderOption). |
| stop_on_invalid_record   | boolean | optional    | `true`                                  | |
| default_timestamp_format | string  | optional    | `'%Y-%m-%d %H:%M:%S.%N %z'`             | |
| default_timezone         | string  | optional    | `'UTC'`                                 | |
| default_typecast         | string  | optional    | `'strict'`                              | |
| columns                  | array   | required    |                                         | |

##### about keyfile

* if `auth_method` is `compute_engine` or `application_default`, this option is not required.
* if `auth_method` is `authorized_user`, this plugin supposes the format is the below.
  ```json
  {
    "client_id":"xxxxxxxxxxx.apps.googleusercontent.com",
    "client_secret":"xxxxxxxxxxx",
    "refresh_token":"xxxxxxxxxxx"
  }
  ```
* if `auth_method` is `service_account`, set the service account credential json file path.

##### about columns

* name: column name
* type: boolean, long, double, string, timestamp, json
* format: timestamp format like `'%Y-%m-%d %H:%M:%S.%N %z'`
* timezone: timezone
* typecast: you can choose `strict`, `loose`, `minimal` (default: `strict`)
  * `strict`: raise TypecastError if typecasting is failed.
  * `loose` : set `NULL` value if typecasting is failed.
  * `minimal` : typecast minimally.

## Development

### Run test:

```
$ ./gradlew test
```
