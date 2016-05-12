#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

__visible struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0x141d70f7, __VMLINUX_SYMBOL_STR(module_layout) },
	{ 0x6e7f4f74, __VMLINUX_SYMBOL_STR(single_release) },
	{ 0xe5f29f12, __VMLINUX_SYMBOL_STR(seq_read) },
	{ 0x2698a4b0, __VMLINUX_SYMBOL_STR(seq_lseek) },
	{ 0x27e1a049, __VMLINUX_SYMBOL_STR(printk) },
	{ 0xe790af54, __VMLINUX_SYMBOL_STR(proc_create_data) },
	{ 0x91831d70, __VMLINUX_SYMBOL_STR(seq_printf) },
	{ 0x553321aa, __VMLINUX_SYMBOL_STR(init_task) },
	{ 0x40c7247c, __VMLINUX_SYMBOL_STR(si_meminfo) },
	{ 0x31fa954f, __VMLINUX_SYMBOL_STR(single_open) },
	{ 0xbdfb6dbb, __VMLINUX_SYMBOL_STR(__fentry__) },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "86CFF1DD9D1CDA9BA386D2F");
