// pages/cart.js
const {
  getCart
} = require("../../api/cart")
const {
  formatPrice
} = require("../../utils/format")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    items: [],
    totalQuantity: 0,
    totalAmountText: "0.00",

    loading: true,
    errorMessage: ""
  },

  loadCart() {
    this.setData({
      loading: true,
      errorMessage: ""
    })

    getCart()
      .then((cart) => {
        const items = cart.items.map((item) => {
          return {
            ...item,
            dishPriceText: formatPrice(item.dishPrice),
            amountText: formatPrice(item.amount)
          }
        })

        this.setData({
          items,
          totalQuantity: cart.totalQuantity,
          totalAmountText: formatPrice(cart.totalAmount),
          loading: false
        })
      })
      .catch((err) => {
        console.error("购物车加载失败：", err)

        this.setData({
          items: [],
          totalQuantity: 0,
          totalAmountText: "0.00",
          loading: false,
          errorMessage: err.message || "购物车加载失败"
        })
      })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 每次购物车页面显示时重新查询。
   *
   * 使用 onShow 而不是只使用 onLoad，
   * 是因为用户从菜品详情页加入购物车后，
   * 再次进入购物车时需要获取最新数据。
   */
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.loadCart()
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