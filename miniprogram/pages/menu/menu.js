// pages/menu.js
const {
  getCategories
} = require("../../api/category")
const {
  getDishes
} = require("../../api/dish")
const {
  formatPrice
} = require("../../utils/format")
const {
  addCartItem,
  getCart
} = require("../../api/cart")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    categories: [],
    activeCategoryId: null,
    currentDishes: [],

    categoryLoading: true,
    dishLoading: false,

    cartTotalQuantity: 0,
    cartTotalAmountText: "0.00"
  },

  onCategoryTap(event) {
    const categoryId = Number(event.currentTarget.dataset.id)

    this.setData({
      activeCategoryId: categoryId
    })

    this.loadDishes(categoryId)
  },

  onDishTap(event) {
    const dishId = Number(event.currentTarget.dataset.id)

    wx.navigateTo({
      url: `/pages/dish-detail/dish-detail?id=${dishId}`
    })
  },

  onAddCartTap(event) {
    const dishId = Number(event.currentTarget.dataset.id)
  
    addCartItem({
      dishId,
      quantity: 1
    })
      .then((cart) => {
        this.updateCartSummary(cart)
  
        wx.showToast({
          title: "已加入购物车",
          icon: "success"
        })
      })
      .catch((err) => {
        console.error("加入购物车失败：", err)
      })
  },

  loadCategories() {
    this.setData({
      categoryLoading: true
    })

    getCategories()
      .then((categories) => {
        const activeCategoryId =
          categories.length > 0 ? categories[0].id : null

        this.setData({
          categories,
          activeCategoryId,
          categoryLoading: false,
          currentDishes: []
        })

        if (activeCategoryId !== null) {
          this.loadDishes(activeCategoryId)
        }
      })
      .catch((err) => {
        console.error("分类加载失败：", err)

        this.setData({
          categoryLoading: false
        })
      })
  },

  loadDishes(categoryId) {
    this.setData({
      dishLoading: true,
      currentDishes: []
    })

    getDishes(categoryId)
      .then((dishes) => {
        const formattedDishes = dishes.map((dish) => {
          return {
            ...dish,
            priceText: formatPrice(dish.price)
          }
        })

        this.setData({
          currentDishes: formattedDishes,
          dishLoading: false
        })
      })
      .catch((err) => {
        console.error("菜品加载失败：", err)

        this.setData({
          currentDishes: [],
          dishLoading: false
        })
      })
  },

  loadCartSummary() {
    getCart()
      .then((cart) => {
        this.updateCartSummary(cart)
      })
      .catch((err) => {
        console.error("购物车汇总加载失败：", err)
      })
  },

  updateCartSummary(cart) {
    this.setData({
      cartTotalQuantity: cart.totalQuantity,
      cartTotalAmountText: formatPrice(cart.totalAmount)
    })
  },
  
  onGoCart() {
    wx.navigateTo({
      url: "/pages/cart/cart"
    })
  },
  
  onCheckoutTap() {
    if (this.data.cartTotalQuantity === 0) {
      return
    }
  
    wx.showToast({
      title: "确认订单页下一阶段开发",
      icon: "none"
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    this.loadCategories()
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
    this.loadCartSummary()
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