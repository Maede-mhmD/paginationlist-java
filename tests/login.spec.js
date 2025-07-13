const { test, expect } = require('@playwright/test');

test('ورود موفق با اطلاعات صحیح', async ({ page }) => {
  await page.goto('http://localhost:3000/login');
  
  // پر کردن فرم
  await page.fill('input[type="text"]', 'admin');
  await page.fill('input[type="password"]', 'admin123');
  await page.click('button[type="submit"]');
  
  // انتظار برای ریدایرکت
  await page.waitForURL('http://localhost:3000/');
  await expect(page).toHaveURL('http://localhost:3000/');
});

test('ورود ناموفق با اطلاعات نادرست', async ({ page }) => {
  await page.goto('http://localhost:3000/login');
  
  await page.fill('input[type="text"]', 'wronguser');
  await page.fill('input[type="password"]', 'wrongpass');
  await page.click('button[type="submit"]');
  
  // بررسی نمایش پیام خطا
  await expect(page.locator('.message.error')).toBeVisible();
});