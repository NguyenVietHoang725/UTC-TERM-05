using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using MyWebApp.Models;
using MyWebApp.Services;

namespace MyWebApp.Controllers
{
    public class StudentController : Controller
    {
        private readonly List<Student> _listStudents = new List<Student>();
        private readonly IBufferedFileUploadService _bufferedFileUploadService;

        public StudentController(IBufferedFileUploadService bufferedFileUploadService)
        {
            _bufferedFileUploadService = bufferedFileUploadService;

            _listStudents = new List<Student>()
            {
                new Student()
                {
                    Id = 101,
                    Name = "Hải Nam",
                    Branch = Branch.IT,
                    Gender = Gender.Male,
                    IsRegular = true,
                    Address = "A1-2018",
                    Email = "nam@g.com"
                },
                new Student()
                {
                    Id = 102,
                    Name = "Minh Tú",
                    Branch = Branch.BE,
                    Gender = Gender.Female,
                    IsRegular = true,
                    Address = "A1-2019",
                    Email = "tu@g.com"
                },
                new Student()
                {
                    Id = 103,
                    Name = "Hoàng Phong",
                    Branch = Branch.CE,
                    Gender = Gender.Male,
                    IsRegular = false,
                    Address = "A1-2020",
                    Email = "phong@g.com"
                },
                new Student()
                {
                    Id = 104,
                    Name = "Xuân Mai",
                    Branch = Branch.EE,
                    Gender = Gender.Female,
                    IsRegular = false,
                    Address = "A1-2021",
                    Email = "mai@g.com"
                }
            };
        }

        public IActionResult Index()
        {
            return View(_listStudents);
        }

        [HttpGet]
        public IActionResult Create()
        {
            ViewBag.AllGenders = Enum.GetValues(typeof(Gender)).Cast<Gender>().ToList();
            ViewBag.AllBranches = new List<SelectListItem>()
            {
                new SelectListItem() { Text = "IT", Value = "1" },
                new SelectListItem() { Text = "BE", Value = "2" },
                new SelectListItem() { Text = "CE", Value = "3" },
                new SelectListItem() { Text = "EE", Value = "4" }
            };
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Create(Student s, IFormFile file)
        {
            if (file != null && file.Length > 0)
            {
                try
                {
                    if (await _bufferedFileUploadService.UploadFile(file))
                    {
                        s.ImagePath = file.FileName; // Lưu đường dẫn tương đối
                        ViewBag.Message = "File Upload Successful";
                    }
                    else
                    {
                        ViewBag.Message = "File Upload Failed";
                    }
                }
                catch (Exception ex)
                {
                    ViewBag.Message = "File Upload Failed: " + ex.Message;
                    ViewBag.AllGenders = Enum.GetValues(typeof(Gender)).Cast<Gender>().ToList();
                    ViewBag.AllBranches = new List<SelectListItem>()
                    {
                        new SelectListItem() { Text = "IT", Value = "1" },
                        new SelectListItem() { Text = "BE", Value = "2" },
                        new SelectListItem() { Text = "CE", Value = "3" },
                        new SelectListItem() { Text = "EE", Value = "4" }
                    };
                    return View(s);
                }
            }

            s.Id = _listStudents.Last<Student>().Id + 1;
            _listStudents.Add(s);
            return View("Index", _listStudents);
        }
    }
}