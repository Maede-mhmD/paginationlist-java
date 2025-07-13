const { test, expect } = require('@playwright/test');

test.describe('مدیریت کاربران', () => {
  test.beforeEach(async ({ page }) => {
    // لاگین قبل از هر تست
    await page.goto('http://localhost:3000/login');
    await page.fill('input[type="text"]', 'admin');
    await page.fill('input[type="password"]', 'admin123');
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/');
  });

  test('افزودن کاربر جدید', async ({ page }) => {
    await page.goto('http://localhost:3000/create-user');
    
    await page.fill('input[name="name"]', 'کاربر تست');
    await page.fill('input[name="age"]', '30');
    await page.fill('input[name="city"]', 'تهران');
    await page.fill('input[name="job"]', 'توسعه دهنده');
    await page.click('.add-user-btn-submit');
    
    await expect(page.locator('.add-user-message.success')).toContainText('کاربر با موفقیت اضافه شد');
  });

  test('مشاهده لیست کاربران', async ({ page }) => {
    await page.goto('http://localhost:3000/');
    await expect(page.locator('table')).toBeVisible();
    await expect(page.locator('tr')).toHaveCountGreaterThan(1);
  });
});