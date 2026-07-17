// pages/cart.js
const {
  getCart,
  updateCartItemQuantity,
  deleteCartItem,
  clearCart
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
    errorMessage: "",

    updatingCartItemId: null,

    clearingCart: false
  },

  loadCart() {
    this.setData({
      loading: true,
      errorMessage: ""
    })

    getCart()
      .then((cart) => {
        this.updateCartView(cart)

        this.setData({
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

  updateCartView(cart) {
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
      totalAmountText: formatPrice(cart.totalAmount)
    })
  },

  onIncreaseTap(event) {
    if (this.data.updatingCartItemId !== null) {
      return
    }
  
    const cartItemId = Number(event.currentTarget.dataset.id)
    const currentQuantity = Number(event.currentTarget.dataset.quantity)
  
    if (currentQuantity >= 99) {
      wx.showToast({
        title: "数量不能超过99",
        icon: "none"
      })
  
      return
    }
  
    this.updateQuantity(cartItemId, currentQuantity + 1)
  },

  onDecreaseTap(event) {
    if (this.data.updatingCartItemId !== null) {
      return
    }
  
    const cartItemId = Number(event.currentTarget.dataset.id)
    const currentQuantity = Number(event.currentTarget.dataset.quantity)
  
    if (currentQuantity === 1) {
      this.removeCartItem(cartItemId)
      return
    }
  
    this.updateQuantity(cartItemId, currentQuantity - 1)
  },

  updateQuantity(cartItemId, quantity) {
    if (this.data.updatingCartItemId !== null) {
      return
    }

    this.setData({
      updatingCartItemId: cartItemId
    })

    updateCartItemQuantity(cartItemId, quantity)
      .then((cart) => {
        this.updateCartView(cart)

        this.setData({
          updatingCartItemId: null
        })
      })
      .catch((err) => {
        console.error("修改购物车数量失败：", err)

        this.setData({
          updatingCartItemId: null
        })
      })
  },

  removeCartItem(cartItemId) {
    this.setData({
      updatingCartItemId: cartItemId
    })
  
    deleteCartItem(cartItemId)
      .then((cart) => {
        this.updateCartView(cart)
  
        this.setData({
          updatingCartItemId: null
        })
      })
      .catch((err) => {
        console.error("删除购物车菜品失败：", err)
  
        this.setData({
          updatingCartItemId: null
        })
      })
  },

  onClearCartTap() {
    if (
      this.data.clearingCart ||
      this.data.updatingCartItemId !== null
    ) {
      return
    }
  
    wx.showModal({
      title: "清空购物车",
      content: "确定要清空购物车中的全部菜品吗？",
      confirmText: "清空",
      confirmColor: "#e64340",
  
      success: (res) => {
        if (!res.confirm) {
          return
        }
  
        this.setData({
          clearingCart: true
        })
  
        clearCart()
          .then(() => {
            this.setData({
              items: [],
              totalQuantity: 0,
              totalAmountText: "0.00",
              clearingCart: false
            })
  
            wx.showToast({
              title: "购物车已清空",
              icon: "success"
            })
          })
          .catch((err) => {
            console.error("清空购物车失败：", err)
  
            this.setData({
              clearingCart: false
            })
  
            wx.showToast({
              title: err.message || "清空购物车失败",
              icon: "none"
            })
          })
      }
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