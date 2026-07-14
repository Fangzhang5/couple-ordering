const { request } = require("../utils/request")

function getDishes(categoryId) {
  return request({
    url: "/dishes",
    data: {
      categoryId
    }
  })
}

function getDishDetail(id) {
  return request({
    url: `/dishes/${id}`
  })
}

module.exports = {
  getDishes,
  getDishDetail
}