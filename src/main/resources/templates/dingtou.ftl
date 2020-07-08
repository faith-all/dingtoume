<html>
<head>
<title>DingTou.Me</title>
</head>
<body>
<#list allFundOrder as fundOrder>
基金编码：${fundOrder.fundCode}<br>
*当前价格：${fundOrder.nowUnitPrice?c}元<br>
*成本价格：${fundOrder.costPrice?c}元<br>
*持有份额：${fundOrder.holdShare?c}份<br>
*持有金额：${fundOrder.holdPrice?c}元<br>
*上期金额：${fundOrder.preGroupTargetValue?c}元<br>
*目标金额：${fundOrder.groupTargetValue?c}元<br>
*购买份额：${fundOrder.buyShare?c}份<br>
*购买金额：${fundOrder.buyPrice?c}元<br>
*购买费用：${fundOrder.buyFee?c}元<br><br><br>
</#list>
<br><a href="fundmanager.jsp?type=buy&value=${buyParam}&timestamp=${timestamp?c}">完成购买</a>
</body>
</html>