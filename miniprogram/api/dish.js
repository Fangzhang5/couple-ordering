const { request } = require("../utils/request")

function getDishes(categoryId) {
  return request({
    url: "/dishes",
    data: {
      categoryId
    }
  })
}

module.exports = {
  getDishes
}