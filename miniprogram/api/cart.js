const { request } = require("../utils/request")


/**
 * 加入购物车
 */
function addCartItem(data) {

  return request({
    url: "/cart/items",
    method: "POST",
    data
  })

}


/**
 * 查询购物车
 */
function getCart() {

  return request({
    url: "/cart/items",
    method: "GET"
  })

}


module.exports = {
  addCartItem,
  getCart
}