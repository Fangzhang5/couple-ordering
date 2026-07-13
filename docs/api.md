# 小程序点单系统 API 接口文档

## 1. 文档信息

- API 版本：v1
- 基础路径：`/api/v1`
- 本地地址：`http://localhost:8080/api/v1`
- 数据格式：JSON
- 字符编码：UTF-8
- 时间格式：`yyyy-MM-dd HH:mm:ss`
- 金额格式：十进制数字，单位为元，例如 `12.50`

当前第一阶段优先实现带有 **P0 第一阶段** 标记的接口，其余接口用于后续开发规划。

---

## 2. 通用约定

### 2.1 请求头

无需登录的接口：

```http
Content-Type: application/json
```

完成微信登录后，需要登录的接口增加：

```http
Authorization: Bearer <JWT>
Content-Type: application/json
```

### 2.2 统一成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

### 2.3 统一失败响应

```json
{
  "code": 40001,
  "message": "请求参数不正确",
  "data": null
}
```

### 2.4 建议业务错误码

| code | 含义 |
|---:|---|
| 0 | 成功 |
| 40001 | 请求参数错误 |
| 40002 | 业务规则校验失败 |
| 40101 | 未登录或 Token 无效 |
| 40301 | 无操作权限 |
| 40401 | 资源不存在 |
| 40901 | 数据状态冲突 |
| 50001 | 系统内部错误 |

### 2.5 分页参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|---|---|---:|---:|---|
| page | Integer | 否 | 1 | 页码，从 1 开始 |
| pageSize | Integer | 否 | 10 | 每页条数，最大 100 |

分页响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "page": 1,
    "pageSize": 10,
    "total": 2,
    "records": []
  }
}
```

---

# 3. 基础接口

## 3.1 健康检查【P0 第一阶段】

```http
GET /api/v1/health
```

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "status": "UP"
  }
}
```

---

# 4. 用户认证

## 4.1 微信登录【后续阶段】

```http
POST /api/v1/auth/wechat-login
```

请求体：

```json
{
  "code": "wx.login 返回的临时登录凭证"
}
```

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "JWT_TOKEN",
    "user": {
      "id": 1,
      "nickname": "小郭",
      "avatarUrl": "https://example.com/avatar.jpg",
      "role": 1
    }
  }
}
```

说明：

- 后端使用临时 `code` 向微信服务器换取 `openid`。
- `openid` 仅保存在服务端，不直接返回给小程序。
- `role = 0` 表示普通用户，`role = 1` 表示管理员。
- 只允许已启用的白名单用户登录。

## 4.2 查询当前用户【后续阶段】

```http
GET /api/v1/users/me
Authorization: Bearer <JWT>
```

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "nickname": "小郭",
    "avatarUrl": "https://example.com/avatar.jpg",
    "role": 1,
    "status": 1
  }
}
```

---

# 5. 分类接口

## 5.1 查询已启用分类【P0 第一阶段】

```http
GET /api/v1/categories
```

查询规则：

- 只返回 `status = 1` 的分类。
- 按 `sort ASC, id ASC` 排序。

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "家常菜",
      "sort": 1
    },
    {
      "id": 2,
      "name": "主食",
      "sort": 2
    }
  ]
}
```

## 5.2 查询分类详情【可选】

```http
GET /api/v1/categories/{id}
```

---

# 6. 菜品接口

## 6.1 查询菜品列表【P0 第一阶段】

```http
GET /api/v1/dishes
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
|---|---|---:|---|
| categoryId | Long | 否 | 分类 ID |
| keyword | String | 否 | 菜品名称关键字 |
| status | Integer | 否 | 普通用户接口默认且只能查询 1 |

请求示例：

```http
GET /api/v1/dishes?categoryId=1
```

查询规则：

- 普通用户只返回 `status = 1` 的菜品。
- 按 `id ASC` 排序。
- 第一阶段不分页；菜品较多后再增加分页。

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "categoryId": 1,
      "categoryName": "家常菜",
      "name": "番茄炒蛋",
      "price": 12.00,
      "imageUrl": "",
      "description": "酸甜家常口味",
      "status": 1
    }
  ]
}
```

## 6.2 查询菜品详情【P0 第一阶段】

```http
GET /api/v1/dishes/{id}
```

成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "categoryId": 1,
    "categoryName": "家常菜",
    "name": "番茄炒蛋",
    "price": 12.00,
    "imageUrl": "",
    "description": "酸甜家常口味",
    "status": 1
  }
}
```

菜品不存在或已下架：

```http
HTTP/1.1 404 Not Found
```

```json
{
  "code": 40401,
  "message": "菜品不存在或已下架",
  "data": null
}
```

---

# 7. 购物车接口

以下接口均需要登录。

## 7.1 查询购物车【后续阶段】

```http
GET /api/v1/cart/items
```

响应示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "dishId": 1,
        "dishName": "番茄炒蛋",
        "dishImageUrl": "",
        "dishPrice": 12.00,
        "quantity": 2,
        "amount": 24.00
      }
    ],
    "totalQuantity": 2,
    "totalAmount": 24.00
  }
}
```

## 7.2 加入购物车【后续阶段】

```http
POST /api/v1/cart/items
```

请求体：

```json
{
  "dishId": 1,
  "quantity": 1
}
```

规则：

- 菜品必须存在且已上架。
- 同一用户、同一菜品只保留一条购物车记录。
- 重复加入时累加数量。
- 数量必须大于 0。

## 7.3 修改购物车数量【后续阶段】

```http
PUT /api/v1/cart/items/{id}
```

请求体：

```json
{
  "quantity": 3
}
```

## 7.4 删除购物车项【后续阶段】

```http
DELETE /api/v1/cart/items/{id}
```

## 7.5 清空购物车【后续阶段】

```http
DELETE /api/v1/cart/items
```

---

# 8. 用户订单接口

以下接口均需要登录。

## 8.1 提交订单【后续阶段】

```http
POST /api/v1/orders
```

请求体：

```json
{
  "remark": "少盐，不要葱",
  "expectedTime": "2026-07-13 18:30:00"
}
```

业务规则：

1. 当前用户购物车不能为空。
2. 只结算当前用户自己的购物车。
3. 所有菜品必须仍然存在且处于上架状态。
4. 后端重新读取菜品价格并计算订单金额，不能信任前端金额。
5. 创建订单及订单明细必须处于同一个数据库事务中。
6. 创建成功后清空当前用户购物车。
7. 订单明细保存菜品名称、图片、单价快照。

成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "orderNumber": "202607131830001001",
    "status": 1,
    "amount": 24.00,
    "remark": "少盐，不要葱",
    "expectedTime": "2026-07-13 18:30:00",
    "createTime": "2026-07-13 17:55:00"
  }
}
```

## 8.2 查询我的订单【后续阶段】

```http
GET /api/v1/orders?page=1&pageSize=10&status=1
```

## 8.3 查询我的订单详情【后续阶段】

```http
GET /api/v1/orders/{id}
```

只能查询当前用户自己的订单。

## 8.4 取消订单【后续阶段】

```http
PUT /api/v1/orders/{id}/cancel
```

请求体：

```json
{
  "reason": "临时不吃了"
}
```

规则：

- 仅待接单状态允许用户取消。
- 已接单、制作中和已完成订单不能由用户取消。

---

# 9. 管理端分类接口

以下接口要求管理员权限。

## 9.1 查询全部分类【后续阶段】

```http
GET /api/v1/admin/categories
```

## 9.2 新增分类【后续阶段】

```http
POST /api/v1/admin/categories
```

请求体：

```json
{
  "name": "饮品",
  "sort": 4,
  "status": 1
}
```

## 9.3 修改分类【后续阶段】

```http
PUT /api/v1/admin/categories/{id}
```

## 9.4 修改分类状态【后续阶段】

```http
PATCH /api/v1/admin/categories/{id}/status
```

请求体：

```json
{
  "status": 0
}
```

## 9.5 删除分类【后续阶段】

```http
DELETE /api/v1/admin/categories/{id}
```

规则：分类下存在菜品时不允许删除，应先处理菜品。

---

# 10. 管理端菜品接口

以下接口要求管理员权限。

## 10.1 查询全部菜品【后续阶段】

```http
GET /api/v1/admin/dishes?page=1&pageSize=10&categoryId=1&status=1&keyword=番茄
```

## 10.2 新增菜品【后续阶段】

```http
POST /api/v1/admin/dishes
```

请求体：

```json
{
  "categoryId": 1,
  "name": "可乐鸡翅",
  "price": 28.00,
  "imageUrl": "https://example.com/dish.jpg",
  "description": "甜咸口味",
  "status": 1
}
```

## 10.3 修改菜品【后续阶段】

```http
PUT /api/v1/admin/dishes/{id}
```

## 10.4 修改菜品状态【后续阶段】

```http
PATCH /api/v1/admin/dishes/{id}/status
```

请求体：

```json
{
  "status": 0
}
```

## 10.5 删除菜品【后续阶段】

```http
DELETE /api/v1/admin/dishes/{id}
```

建议优先下架而不是物理删除。购物车仍引用该菜品时不允许删除。

---

# 11. 管理端订单接口

以下接口要求管理员权限。

## 11.1 查询全部订单【后续阶段】

```http
GET /api/v1/admin/orders?page=1&pageSize=10&status=1
```

## 11.2 查询订单详情【后续阶段】

```http
GET /api/v1/admin/orders/{id}
```

## 11.3 修改订单状态【后续阶段】

```http
PATCH /api/v1/admin/orders/{id}/status
```

请求体：

```json
{
  "status": 2
}
```

允许的主要状态流转：

```text
待接单(1) → 已接单(2) → 制作中(3) → 已完成(4)
待接单(1) → 已取消(5)
已接单(2) → 已取消(5)，仅管理员特殊处理
```

---

# 12. 状态枚举

## 12.1 用户角色

| 值 | 含义 |
|---:|---|
| 0 | 普通用户 |
| 1 | 管理员 |

## 12.2 通用启用状态

| 值 | 含义 |
|---:|---|
| 0 | 禁用、下架 |
| 1 | 启用、上架 |

## 12.3 订单状态

| 值 | 含义 |
|---:|---|
| 1 | 待接单 |
| 2 | 已接单 |
| 3 | 制作中 |
| 4 | 已完成 |
| 5 | 已取消 |

---

# 13. 第一阶段接口清单

第一阶段只需要完成并联调：

| 接口 | 用途 |
|---|---|
| `GET /api/v1/health` | 验证后端启动 |
| `GET /api/v1/categories` | 获取分类列表 |
| `GET /api/v1/dishes?categoryId={id}` | 获取分类下菜品 |
| `GET /api/v1/dishes/{id}` | 获取菜品详情 |

第一阶段完成后，再按照“购物车 → 订单 → 登录权限 → 管理端”的顺序实现。
