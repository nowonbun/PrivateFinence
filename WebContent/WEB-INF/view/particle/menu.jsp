<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ul class="sidebar navbar-nav toggled">
	<c:forEach items="${menu}" var="item">
	    <li class="nav-item"  data-toggle="tooltip" data-placement="right" title="${item.name}">
	    	<a class="nav-link" href="${item.link}"> 
				<i class="${item.icon}"></i> <span>${item.name}</span>
			</a>
		</li>
	</c:forEach>
</ul>