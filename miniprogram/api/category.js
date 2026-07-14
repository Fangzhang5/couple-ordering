const { request } = require("../utils/request")

function getCategories() {
  return request({
    url: "/categories"
  })
}

module.exports = {
  getCategories
}