// pages/menu/dish-detail.js
const {
  getDishDetail
} = require("../../api/dish")
const {
  formatPrice
} = require("../../utils/format")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    dish: null,
    loading: true,
    errorMessage: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log("详情页参数：", options)

    const dishId = Number(options.id)

    if (!dishId) {
      this.setData({
        loading: false,
        errorMessage: "菜品参数不正确"
      })

      return
    }

    this.loadDishDetail(dishId)
  },

  loadDishDetail(dishId) {
    this.setData({
      loading: true,
      errorMessage: ""
    })

    getDishDetail(dishId)
      .then((dish) => {
        const formattedDish = {
          ...dish,
          priceText: formatPrice(dish.price)
        }

        this.setData({
          dish: formattedDish,
          loading: false
        })
      })
      .catch((err) => {
        console.error("菜品详情加载失败：", err)

        this.setData({
          dish: null,
          loading: false,
          errorMessage: err.message || "菜品加载失败"
        })
      })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})