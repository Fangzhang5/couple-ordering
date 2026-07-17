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
  getCart,
  updateCartItemQuantity,
  deleteCartItem
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

    cartItems: [],
    cartTotalQuantity: 0,
    cartTotalAmountText: "0.00",

    updatingDishId: null
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
    if (this.data.updatingDishId !== null) {
      return
    }
  
    const dishId = Number(event.currentTarget.dataset.id)
  
    this.setData({
      updatingDishId: dishId
    })
  
    addCartItem({
      dishId,
      quantity: 1
    })
      .then((cart) => {
        this.updateCartSummary(cart)
  
        this.setData({
          updatingDishId: null
        })
      })
      .catch((err) => {
        console.error("加入购物车失败：", err)
  
        this.setData({
          updatingDishId: null
        })
  
        wx.showToast({
          title: err.message || "加入购物车失败",
          icon: "none"
        })
      })
  },

  onDecreaseCartTap(event) {
    if (this.data.updatingDishId !== null) {
      return
    }
  
    const dishId = Number(event.currentTarget.dataset.dishId)
    const cartItemId = Number(event.currentTarget.dataset.cartItemId)
    const currentQuantity = Number(event.currentTarget.dataset.quantity)
  
    if (!cartItemId || currentQuantity <= 0) {
      return
    }
  
    this.setData({
      updatingDishId: dishId
    })
  
    const requestTask =
      currentQuantity === 1
        ? deleteCartItem(cartItemId)
        : updateCartItemQuantity(cartItemId, currentQuantity - 1)
  
    requestTask
      .then((cart) => {
        this.updateCartSummary(cart)
  
        this.setData({
          updatingDishId: null
        })
      })
      .catch((err) => {
        console.error("减少购物车菜品失败：", err)
  
        this.setData({
          updatingDishId: null
        })
  
        wx.showToast({
          title: err.message || "操作失败",
          icon: "none"
        })
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

        const dishesWithCartInfo = this.mergeCartInfoToDishes(
          formattedDishes,
          this.data.cartItems
        )

        this.setData({
          currentDishes: dishesWithCartInfo,
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

  mergeCartInfoToDishes(dishes, cartItems) {
    const cartItemMap = {}

    cartItems.forEach((cartItem) => {
      cartItemMap[cartItem.dishId] = cartItem
    })

    return dishes.map((dish) => {
      const cartItem = cartItemMap[dish.id]

      return {
        ...dish,
        cartItemId: cartItem ? cartItem.id : null,
        cartQuantity: cartItem ? cartItem.quantity : 0
      }
    })
  },

  updateCartSummary(cart) {
    const cartItems = cart.items || []
  
    const currentDishes = this.mergeCartInfoToDishes(
      this.data.currentDishes,
      cartItems
    )
  
    this.setData({
      cartItems,
      currentDishes,
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