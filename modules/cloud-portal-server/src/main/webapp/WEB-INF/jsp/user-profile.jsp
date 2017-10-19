<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%--@elvariable id="self" type="de.papke.cloud.portal.model.Data"--%>

<jsp:include page="header.jsp" />

<div id="wrapper">

	<jsp:include page="navigation.jsp" />

	<div id="page-wrapper">
		<div class="row">
			<h1 class="page-header">User Profile</h1>
			<div class="col-lg-12">
                <h2>Groups</h2>
                <ul>
			    <c:forEach items="${self.groupList}" var="group">
			         <li><c:out value="${group}" /></li>
			    </c:forEach>
			    </ul>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<jsp:include page="footer.jsp" />