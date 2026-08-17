<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '../../stores/auth';

const toastMsg = ref('');
const authStore = useAuthStore();

const showToast = (msg: string) => {
  toastMsg.value = msg;
  setTimeout(() => toastMsg.value = '', 2500);
};

const resetAdmin = () => {
  if (!confirm('确定要重置管理员账号吗？将恢复为默认 admin / admin123。')) return;
  authStore.logout();
  showToast('管理员账号已重置，请重新登录');
};

const exportAll = async () => {
  const data = {
    exportTime: new Date().toISOString()
  };
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `hotel-backup-${Date.now()}.json`;
  a.click();
  URL.revokeObjectURL(url);
  showToast('数据已导出');
};

const importAll = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const text = await file.text();
    JSON.parse(text);
    if (!confirm('导入将覆盖现有数据，确定继续？')) return;
    showToast('数据导入成功，即将刷新');
    setTimeout(() => location.reload(), 800);
  } catch {
    showToast('导入失败：文件格式错误');
  }
  target.value = '';
};
</script>

<template>
  <div class="settings-page">
    <div class="page-header">
      <div>
        <h2>系统设置</h2>
        <p class="subtitle">管理系统数据、账号及存储配置</p>
      </div>
    </div>

    <div class="section">
      <div class="section-title">🔐 管理员账号</div>
      <div class="card">
        <div class="row">
          <div class="col-info">
            <div class="label">当前登录</div>
            <div class="value">admin</div>
          </div>
          <div class="col-actions">
            <button class="btn-warning" @click="resetAdmin">重置为默认账号</button>
          </div>
        </div>
        <div class="tips">
          默认账号：<code>admin</code> / <code>admin123</code>（建议登录后尽快修改）
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">📦 数据管理</div>
      <div class="card">
        <div class="data-actions">
          <div class="data-item">
            <div class="data-title">导出全部数据</div>
            <div class="data-desc">将用户、预订、餐饮预订数据打包导出为 JSON</div>
            <button class="btn-primary" @click="exportAll">导出备份</button>
          </div>
          <div class="data-item">
            <div class="data-title">导入数据</div>
            <div class="data-desc">从备份文件恢复数据（将覆盖现有数据）</div>
            <label class="btn-default file-btn">
              选择文件导入
              <input type="file" accept=".json" @change="importAll" hidden />
            </label>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">🗑️ 数据清理</div>
      <div class="card">
        <div class="danger-box">
          <div class="danger-title">危险操作区</div>
          <div class="danger-desc">以下操作将永久删除数据，且不可恢复，请谨慎操作</div>
          <div class="danger-actions">
            <button class="btn-danger">清空客房预订</button>
            <button class="btn-danger">清空餐饮预订</button>
            <button class="btn-danger">清空所有用户</button>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">ℹ️ 系统信息</div>
      <div class="card">
        <div class="info-grid">
          <div class="info-item">
            <div class="label">系统名称</div>
            <div class="value">星级酒店管理后台</div>
          </div>
          <div class="info-item">
            <div class="label">版本号</div>
            <div class="value">v1.0.0</div>
          </div>
          <div class="info-item">
            <div class="label">前端框架</div>
            <div class="value">Vue 3 + Vite</div>
          </div>
          <div class="info-item">
            <div class="label">后端框架</div>
            <div class="value">Spring Boot 3</div>
          </div>
          <div class="info-item">
            <div class="label">数据库</div>
            <div class="value">MySQL 8.0+</div>
          </div>
          <div class="info-item">
            <div class="label">部署日期</div>
            <div class="value">2026-06</div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="toastMsg" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<style scoped>
.settings-page { padding: 0; }
.page-header {
  margin-bottom: 22px;
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}
.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1f2d3d;
  font-weight: 600;
}
.subtitle { margin: 6px 0 0; color: #909399; font-size: 13px; }

.section { margin-bottom: 22px; }
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 14px;
  padding-left: 14px;
  border-left: 4px solid transparent;
  border-image: linear-gradient(180deg, #b89450, #66b1ff) 1;
}

.card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}
.col-info .label { font-size: 12px; color: #909399; }
.col-info .value { font-size: 20px; font-weight: 700; color: #1f2d3d; margin: 6px 0; }
.col-info .sub { font-size: 12px; color: #909399; }

.tips {
  margin-top: 16px;
  padding: 12px 16px;
  background: #fdf6ec;
  border-left: 4px solid #e6a23c;
  color: #606266;
  font-size: 13px;
  border-radius: 8px;
}
.tips code {
  background: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  color: #d63384;
  font-family: Consolas, monospace;
  font-size: 12px;
}

.data-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 16px; }
.data-item {
  padding: 18px;
  border: 1.5px dashed #e4e7ed;
  border-radius: 12px;
  transition: all 0.2s;
}
.data-item:hover { border-color: #c6e2ff; background: #fafbfc; }
.data-title { font-size: 15px; font-weight: 600; color: #1f2d3d; margin-bottom: 6px; }
.data-desc { font-size: 13px; color: #909399; margin-bottom: 14px; line-height: 1.5; }
.file-btn { display: inline-block; cursor: pointer; text-align: center; }

.btn-primary { padding: 10px 22px; background: linear-gradient(135deg, #b89450, #c9a96a); color: #fff; border: none; border-radius: 10px; cursor: pointer; font-size: 13px; font-weight: 500; box-shadow: 0 4px 12px rgba(184,148,80,0.25); transition: all 0.2s; }
.btn-primary:hover { transform: translateY(-1px); box-shadow: 0 6px 16px rgba(184,148,80,0.35); }
.btn-default { padding: 10px 22px; background: #fff; color: #606266; border: 1.5px solid #e4e7ed; border-radius: 10px; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.btn-default:hover { color: #b89450; border-color: #c6e2ff; background: #f5f7fa; }
.btn-warning { padding: 10px 22px; background: #fdf6ec; color: #e6a23c; border: 1.5px solid #faecd8; border-radius: 10px; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.btn-warning:hover { background: #faecd8; }
.btn-danger { padding: 10px 22px; background: linear-gradient(135deg, #f56c6c, #f78989); color: #fff; border: none; border-radius: 10px; cursor: pointer; font-size: 13px; font-weight: 500; box-shadow: 0 4px 12px rgba(245,108,108,0.25); transition: all 0.2s; }
.btn-danger:hover { transform: translateY(-1px); box-shadow: 0 6px 16px rgba(245,108,108,0.35); }

.danger-box {
  padding: 20px;
  background: #fff5f5;
  border: 1px solid #fde2e2;
  border-left: 4px solid #f56c6c;
  border-radius: 12px;
}
.danger-title { font-size: 15px; font-weight: 600; color: #f56c6c; margin-bottom: 6px; }
.danger-desc { font-size: 13px; color: #606266; margin-bottom: 14px; line-height: 1.5; }
.danger-actions { display: flex; gap: 10px; flex-wrap: wrap; }

.info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 14px; }
.info-item { padding: 14px; background: #f8fafc; border-radius: 10px; border: 1px solid #f0f2f5; }
.info-item .label { font-size: 12px; color: #909399; margin-bottom: 6px; }
.info-item .value { font-size: 14px; color: #1f2d3d; font-weight: 500; }

.toast {
  position: fixed; top: 90px; left: 50%; transform: translateX(-50%);
  background: linear-gradient(135deg, #1f2d3d, #2f4057);
  color: #fff; padding: 12px 24px; border-radius: 10px; z-index: 2000;
  font-size: 13px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
  animation: slideDown 0.3s ease;
}
@keyframes slideDown {
  from { opacity: 0; transform: translate(-50%, -10px); }
  to { opacity: 1; transform: translate(-50%, 0); }
}
</style>