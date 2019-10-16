## recyclerView 可滚动的背景图实现

**通常设置背景图，背景不会跟随滚动，如下效果**

![image]( https://github.com/yeideal/reclyerviewbg/blob/master/demo-fixed.gif)

**交互想要达到的效果，是会跟着背景图滚动，如下效果**

![image]( https://github.com/yeideal/reclyerviewbg/blob/master/demo-scroll.gif)

要实现这样的效果，用ScrollView嵌套recyclerView可能出现一些性能问题。

这里介绍用通过RecyclerView.ItemDecoration实现。

首先，因为屏幕尺寸和图片尺寸存在比例关系，所有的尺寸都要用比例运算过之后的尺寸

然后，通过第一个item得到滚动偏移量，拼接出当前所处的背景图的bitmap，最后drawBitmap