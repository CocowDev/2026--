<script setup lang="ts">
import { ref } from 'vue'

const milestones = ref([
  { year: '2018', title: '酒店开业', desc: '正式对外营业，开启服务之旅' },
  { year: '2019', title: '荣获三星', desc: '通过星级评定，成为三星级酒店' },
  { year: '2020', title: '全面升级', desc: '完成客房改造，提升服务品质' },
  { year: '2021', title: '智能入住', desc: '引入智能系统，实现自助入住' },
  { year: '2022', title: '绿色认证', desc: '获得绿色酒店认证，践行环保理念' },
  { year: '2023', title: '数字转型', desc: '推出在线预订系统，拥抱数字化' },
  { year: '2024', title: '餐饮焕新', desc: '四大主题餐厅升级，引入星级主厨团队' },
  { year: '2025', title: '设施扩展', desc: '恒温泳池与智能健身房投入使用，服务全面升级' }
])

const teamMembers = ref([
  { name: '张明华', role: '总经理', avatar: 'https://picsum.photos/seed/team1/100/100' },
  { name: '李芳', role: '运营总监', avatar: 'https://picsum.photos/seed/team2/100/100' },
  { name: '王志强', role: '客房部经理', avatar: 'https://picsum.photos/seed/team3/100/100' },
  { name: '陈燕', role: '餐饮部经理', avatar: 'https://picsum.photos/seed/team4/100/100' },
  { name: '刘建国', role: '工程部主管', avatar: 'https://picsum.photos/seed/team5/100/100' },
  { name: '赵雪', role: '市场部经理', avatar: 'https://picsum.photos/seed/team6/100/100' },
  { name: '周婷', role: '前厅部经理', avatar: 'https://picsum.photos/seed/team7/100/100' },
  { name: '孙磊', role: '财务部经理', avatar: 'https://picsum.photos/seed/team8/100/100' }
])

const awards = ref([
  { title: '三星级旅游饭店', issuer: '国家旅游局', year: '2019' },
  { title: '绿色酒店认证', issuer: '中国饭店协会', year: '2022' },
  { title: '最佳服务奖', issuer: '省旅游协会', year: '2020' },
  { title: '消费者满意单位', issuer: '市工商局', year: '2021' },
  { title: '年度人气酒店', issuer: '市文旅局', year: '2023' },
  { title: '文明服务示范单位', issuer: '省服务业联合会', year: '2024' }
])
</script>

<template>
  <div class="about-page">
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-badge">
          <span class="badge-dot"></span>
          ABOUT US · 关于我们
        </span>
        <h1 class="hero-title">
          了解<span class="title-gradient">星级酒店</span>
        </h1>
        <p class="hero-subtitle">
          我们致力于为每一位宾客提供温馨舒适的入住体验，让您在旅途中感受到家的温暖。
          从商务出行到家庭度假，从会议宴请到休闲养生，星级酒店以完善的设施与专业的服务，
          为您打造超越期待的住宿体验。
        </p>
      </div>
    </section>

    <section class="intro-section">
      <div class="container">
        <div class="intro-wrap">
          <div class="intro-image">
            <img src="https://picsum.photos/seed/about-hotel/600/400" alt="酒店外观" @error="(($event.target as HTMLImageElement).src = '/images/room-ocean.jpg')">
            <div class="intro-shape"></div>
          </div>
          <div class="intro-content">
            <span class="section-tag">OUR STORY</span>
            <h2 class="section-title">我们的故事</h2>
            <p class="section-desc">
              星级酒店成立于2018年，坐落于市中心繁华商业区，地理位置优越，交通便利。
              我们拥有200余间精心设计的客房，配备先进的设施和贴心的服务团队。
            </p>
            <p class="section-desc">
              五年来，我们始终坚持"宾客至上"的服务理念，不断提升服务品质，
              赢得了广大宾客的信赖与好评。无论是商务出差还是休闲度假，
              星级酒店都是您理想的选择。
            </p>
            <p class="section-desc">
              酒店内设豪华客房与行政套房共 200 余间，配备全屋智能系统、恒温泳池、
              智能健身房与多功能宴会厅；四大主题餐厅汇聚中西南风味，
              由星级主厨团队倾力打造，为您呈现味蕾盛宴。
            </p>
            <div class="stats-row">
              <div class="stat-item">
                <div class="stat-value">200+</div>
                <div class="stat-label">精品客房</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">50+</div>
                <div class="stat-label">专业员工</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">5年</div>
                <div class="stat-label">品牌历史</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">98%</div>
                <div class="stat-label">宾客满意度</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="mission-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">MISSION & VISION</span>
          <h2 class="section-title">使命与愿景</h2>
          <p class="section-desc">我们的使命是为宾客创造难忘的入住体验，我们的愿景是成为行业标杆</p>
        </div>
        <div class="mission-grid">
          <div class="mission-card">
            <div class="mission-icon" style="background: linear-gradient(135deg, #c9a96a, #b89450);">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
              </svg>
            </div>
            <h3 class="mission-title">核心使命</h3>
            <p class="mission-desc">为每一位宾客提供温馨、舒适、安全的入住体验，让旅途充满温暖与惊喜。从每一次微笑问好到每一个细节服务，我们用心守护您的旅程品质</p>
          </div>
          <div class="mission-card">
            <div class="mission-icon" style="background: linear-gradient(135deg, #e6cf9a, #a8874a);">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
              </svg>
            </div>
            <h3 class="mission-title">企业愿景</h3>
            <p class="mission-desc">成为区域领先的星级酒店品牌，以卓越服务引领行业发展。立足当下、放眼未来，让"星级"成为品质与信赖的代名词</p>
          </div>
          <div class="mission-card">
            <div class="mission-icon" style="background: linear-gradient(135deg, #4facfe, #00f2fe);">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              </svg>
            </div>
            <h3 class="mission-title">服务理念</h3>
            <p class="mission-desc">用心服务，用情沟通，让每一位宾客感受到家的温暖与关怀。我们相信，好的服务藏在细节里——准时、周到、真诚、体贴</p>
          </div>
        </div>
      </div>
    </section>

    <section class="timeline-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">DEVELOPMENT</span>
          <h2 class="section-title">发展历程</h2>
          <p class="section-desc">回顾我们的成长之路，每一步都凝聚着团队的心血与努力</p>
        </div>
        <div class="timeline">
          <div
            v-for="(milestone, index) in milestones"
            :key="index"
            class="timeline-item"
            :class="{ 'is-reverse': index % 2 === 1 }"
          >
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="timeline-year">{{ milestone.year }}</div>
              <h3 class="timeline-title">{{ milestone.title }}</h3>
              <p class="timeline-desc">{{ milestone.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="team-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">TEAM</span>
          <h2 class="section-title">管理团队</h2>
          <p class="section-desc">专业的管理团队，为您提供优质服务保障</p>
        </div>
        <div class="team-grid">
          <div
            v-for="(member, index) in teamMembers"
            :key="index"
            class="team-card"
          >
            <div class="team-avatar">
              <img :src="member.avatar" :alt="member.name" @error="(($event.target as HTMLImageElement).src = '/images/room-single.jpg')">
            </div>
            <h3 class="team-name">{{ member.name }}</h3>
            <p class="team-role">{{ member.role }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="awards-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">AWARDS</span>
          <h2 class="section-title">荣誉资质</h2>
          <p class="section-desc">我们的努力得到了行业和社会的认可</p>
        </div>
        <div class="awards-grid">
          <div
            v-for="(award, index) in awards"
            :key="index"
            class="award-card"
          >
            <div class="award-badge">🏆</div>
            <h3 class="award-title">{{ award.title }}</h3>
            <p class="award-issuer">{{ award.issuer }}</p>
            <span class="award-year">{{ award.year }}</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.about-page {
  min-height: 100vh;
  background: #fafbfc;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  overflow-x: hidden;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.section-header {
  margin-bottom: 48px;
  max-width: 600px;
}

.section-header.center {
  margin-left: auto;
  margin-right: auto;
  text-align: center;
}

.section-tag {
  display: inline-block;
  padding: 6px 14px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: #b89450;
  background: rgba(184, 148, 80, 0.1);
  border-radius: 999px;
  margin-bottom: 16px;
}

.section-title {
  font-size: 38px;
  font-weight: 800;
  color: #1f2937;
  margin: 0 0 16px;
  letter-spacing: -0.5px;
}

.section-desc {
  font-size: 16px;
  line-height: 1.7;
  color: #6b7280;
  margin: 0;
}

.hero-section {
  position: relative;
  padding: 84px 24px 100px;
  overflow: hidden;
  background: linear-gradient(135deg, #0e1c2e 0%, #1c3350 55%, #3a5370 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(255, 255, 255, 0.35) 0%, transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(212, 176, 110, 0.3) 0%, transparent 45%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  color: #fff;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 18px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  margin-bottom: 24px;
}

.badge-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ffd700;
  box-shadow: 0 0 12px rgba(255, 215, 0, 0.8);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.7; }
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  line-height: 1.15;
  margin: 0 0 20px;
  letter-spacing: -1px;
  color: #fff;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.title-gradient {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 50%, #fff5b3 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.hero-subtitle {
  font-size: 18px;
  line-height: 1.7;
  margin: 0;
  color: rgba(255, 255, 255, 0.92);
  max-width: 640px;
  margin-left: auto;
  margin-right: auto;
}

.intro-section {
  padding: 80px 0;
  margin-top: -60px;
  position: relative;
  z-index: 3;
}

.intro-wrap {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 48px;
  align-items: center;
}

.intro-image {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 30px 60px -20px rgba(0, 0, 0, 0.2);
}

.intro-image img {
  width: 100%;
  height: 420px;
  object-fit: cover;
}

.intro-shape {
  position: absolute;
  top: -20px;
  right: -20px;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, rgba(184, 148, 80, 0.3), rgba(240, 147, 251, 0.3));
  border-radius: 50%;
  filter: blur(40px);
}

.intro-content .section-desc {
  margin-bottom: 16px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-top: 32px;
  padding-top: 32px;
  border-top: 1px solid #f3f4f6;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 36px;
  font-weight: 800;
  color: #b89450;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

.mission-section {
  padding: 80px 0;
  background: #fff;
}

.mission-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.mission-card {
  padding: 36px;
  background: #fafbfc;
  border-radius: 20px;
  text-align: center;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
}

.mission-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.mission-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 20px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.mission-icon svg {
  width: 28px;
  height: 28px;
}

.mission-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 12px;
}

.mission-desc {
  font-size: 14px;
  line-height: 1.6;
  color: #6b7280;
  margin: 0;
}

.timeline-section {
  padding: 80px 0;
}

.timeline {
  position: relative;
  max-width: 900px;
  margin: 0 auto;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 50%;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, #c9a96a, #e6cf9a);
  transform: translateX(-50%);
}

.timeline-item {
  position: relative;
  margin-bottom: 48px;
  display: flex;
  align-items: center;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-item.is-reverse {
  flex-direction: row-reverse;
}

.timeline-dot {
  position: absolute;
  left: 50%;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #c9a96a, #e6cf9a);
  border: 4px solid #fff;
  box-shadow: 0 4px 12px rgba(184, 148, 80, 0.4);
  transform: translateX(-50%);
  z-index: 2;
}

.timeline-content {
  width: 45%;
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

.timeline-year {
  font-size: 28px;
  font-weight: 800;
  color: #b89450;
  margin-bottom: 8px;
}

.timeline-title {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px;
}

.timeline-desc {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.team-section {
  padding: 80px 0;
  background: #fff;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 24px;
}

.team-card {
  text-align: center;
  padding: 20px;
  background: #fafbfc;
  border-radius: 16px;
  transition: transform 0.3s ease;
}

.team-card:hover {
  transform: translateY(-4px);
}

.team-avatar {
  width: 80px;
  height: 80px;
  margin: 0 auto 12px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #b89450;
}

.team-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.team-name {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 4px;
}

.team-role {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.awards-section {
  padding: 80px 0 120px;
}

.awards-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.award-card {
  padding: 32px;
  background: #fff;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease;
}

.award-card:hover {
  transform: translateY(-6px);
}

.award-badge {
  font-size: 48px;
  margin-bottom: 16px;
}

.award-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px;
}

.award-issuer {
  font-size: 13px;
  color: #6b7280;
  margin: 0 0 8px;
}

.award-year {
  display: inline-block;
  padding: 4px 12px;
  background: rgba(184, 148, 80, 0.1);
  color: #b89450;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

@media (max-width: 968px) {
  .intro-wrap {
    grid-template-columns: 1fr;
  }

  .mission-grid {
    grid-template-columns: 1fr 1fr;
  }

  .team-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .awards-grid {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 100px 20px 120px;
  }

  .hero-title {
    font-size: 38px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .section-title {
    font-size: 28px;
  }

  .stats-row {
    grid-template-columns: 1fr 1fr;
  }

  .mission-grid {
    grid-template-columns: 1fr;
  }

  .timeline::before {
    left: 20px;
  }

  .timeline-item {
    flex-direction: column !important;
    align-items: flex-start;
  }

  .timeline-dot {
    left: 20px;
    transform: none;
  }

  .timeline-content {
    width: 100%;
    margin-left: 48px;
  }

  .team-grid {
    grid-template-columns: 1fr 1fr;
  }

  .awards-grid {
    grid-template-columns: 1fr;
  }
}
</style>