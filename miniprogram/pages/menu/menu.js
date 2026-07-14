// pages/menu.js
const {
  getCategories
} = require("../../api/category")
const {
  getDishes
} = require("../../api/dish")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    categories: [],
    activeCategoryId: null,
    currentDishes: [],

    categoryLoading: true,
    dishLoading: false
  },

  onCategoryTap(event) {
    const categoryId = Number(event.currentTarget.dataset.id)

    this.setData({
      activeCategoryId: categoryId
    })

    this.loadDishes(categoryId)
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
        this.setData({
          currentDishes: dishes,
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