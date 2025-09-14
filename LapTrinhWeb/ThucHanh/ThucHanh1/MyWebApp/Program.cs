using Microsoft.AspNetCore.Http.Features;
using MyWebApp.Services;

var builder = WebApplication.CreateBuilder(args);

// Thêm Services để hỗ trợ upload file
builder.Services.AddControllersWithViews();
builder.Services.AddTransient<IBufferedFileUploadService, BufferedFileUploadLocalService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
}
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

// Route cho Student/Index -> Admin/Student/List
app.MapControllerRoute(
    name: "StudentList",
    pattern: "Admin/Student/List",
    defaults: new { controller = "Student", action = "Index" });

// Route cho Student/Create -> Admin/Student/Add
app.MapControllerRoute(
    name: "StudentAdd",
    pattern: "Admin/Student/Add",
    defaults: new { controller = "Student", action = "Create" });

// Route cho Upload -> Admin/Upload/File
app.MapControllerRoute(
    name: "FileUpload",
    pattern: "Admin/Upload/File",
    defaults: new { controller = "BufferedFileUpload", action = "Index" });

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
