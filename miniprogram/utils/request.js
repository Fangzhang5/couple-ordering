const { BASE_URL } = require("../config/config")

function request(options) {
  const {
    url,
    method = "GET",
    data = {}
  } = options

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${url}`,
      method,
      data,

      header: {
        "content-type": "application/json"
      },

      success: (res) => {
        console.log("接口返回：", res)

        if (res.statusCode === 200 && res.data.code === 0) {
          resolve(res.data.data)
        } else {
          const message = res.data.message || "请求失败"

          wx.showToast({
            title: message,
            icon: "none"
          })

          reject(new Error(message))
        }
      },

      fail: (err) => {
        console.error("网络请求失败：", err)

        wx.showToast({
          title: "无法连接后端",
          icon: "none"
        })

        reject(err)
      }
    })
  })
}

module.exports = {
  request
}