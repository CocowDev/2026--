<script setup lang="ts">
import { Phone, Mail, MapPin } from 'lucide-vue-next';
</script>

<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>联系我们</h3>
          <div class="contact-item">
            <Phone class="contact-icon" />
            <span>400-888-8888</span>
          </div>
          <div class="contact-item">
            <Mail class="contact-icon" />
            <span>contact@hotel.com</span>
          </div>
          <div class="contact-item">
            <MapPin class="contact-icon" />
            <span>北京市朝阳区建国路88号</span>
          </div>
        </div>
        <div class="footer-section">
          <h3>快速链接</h3>
          <ul>
            <li><a href="/">首页</a></li>
            <li><a href="/booking">在线预订</a></li>
            <li><a href="/login">用户登录</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>关于我们</h3>
          <p>星级酒店预订管理系统致力于为客户提供优质的酒店预订服务，让您的旅途更加舒适愉快。</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 星级酒店预订管理系统 版权所有</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: #1a1a2e;
  color: #fff;
  padding: 2rem 0;
  margin-top: auto;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer-section {
  flex: 1;
}

.footer-section h3 {
  margin-bottom: 1rem;
  color: #e94560;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.contact-icon {
  width: 18px;
  height: 18px;
  color: #e94560;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 8px;
}

.footer-section ul li a {
  color: #fff;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #e94560;
}

.footer-section p {
  line-height: 1.6;
  color: #aaa;
}

.footer-bottom {
  text-align: center;
  padding-top: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: #aaa;
}
</style>