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


/**
 * 修改购物车项数量
 */
function updateCartItemQuantity(cartItemId, quantity) {
  return request({
    url: `/cart/items/${cartItemId}`,
    method: "PATCH",
    data: {
      quantity
    }
  })
}

function deleteCartItem(cartItemId) {
  return request({
    url: `/cart/items/${cartItemId}`,
    method: "DELETE"
  })
}

function clearCart() {
  return request({
    url: "/cart/items",
    method: "DELETE"
  })
}

module.exports = {
  addCartItem,
  getCart,
  updateCartItemQuantity,
  deleteCartItem,
  clearCart
}